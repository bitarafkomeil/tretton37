package com.tretton37.utils;

import com.tretton37.response.CrawlResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Component
public class CrawlingUtils {

    String currentWorkDir = Paths.get("").toAbsolutePath().toString();
    Set<String> internalLinks = new TreeSet<>();
    Set<String> externalLinks = new TreeSet<>();
    Set<String> staticResources = new TreeSet<>();
    Set<String> otherResources = new TreeSet<>();

    public void getLinks(String crawlingUrl, String url, Set<String> urls) {

        if (!urls.add(url))
            return;

        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("a");
            for (Element element : elements) {
                String nextUrl = element.absUrl("href");
                if (nextUrl.isEmpty() || (!nextUrl.isEmpty() && (nextUrl.contains("#"))))
                    continue;
                otherResources.add(nextUrl);
                if (nextUrl.startsWith(crawlingUrl)) {
                    internalLinks.add(nextUrl);
                    getLinks(crawlingUrl, nextUrl, urls);
                } else
                    externalLinks.add(nextUrl);
            }
            Elements staticElements = doc.select("img");
            staticElements.forEach(element -> {
                String staticResource = element.absUrl("src");
                staticResources.add(staticResource);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CrawlResponse crawlResource(String link) {
        getLinks(link, link, new HashSet<>());
        staticResources.parallelStream().forEach(url -> createDirAndDownloadSrc(url, getOnlyFileNameText(url)));
        return new CrawlResponse("Success", internalLinks, externalLinks, staticResources, otherResources);
    }

    public void createDirAndDownloadSrc(String url, String fileName) {
        try {
            Path directory = createDirectory(url);
            try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(directory.toString() + "/" + fileName)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            System.out.println("Create Directory And Download Src: " + e.getMessage() + e);
        }
    }

    public Path createDirectory(String path) throws IOException {
        path = path.replaceAll(":", "");
        path = path.replaceAll("\\?", "");
        Path directory = Paths.get(currentWorkDir, path);
        if (!path.isEmpty()) {
            if (Files.notExists(directory)) {
                System.out.println("Trying To Create Directory " + directory);
                Files.createDirectories(directory);
                System.out.println("Directory is Created Successfully " + directory);
            }
        } else {
            System.out.println("Directory With Empty Path " + path + " Is Not Created!");
        }
        return directory;
    }

    public static String getOnlyFileNameText(String urlText) {
        return urlText.substring(urlText.lastIndexOf("/") + 1);
    }
}