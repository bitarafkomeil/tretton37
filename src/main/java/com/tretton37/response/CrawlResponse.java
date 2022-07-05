package com.tretton37.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * CrawlResponse to map the response
 *
 * @author RV
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawlResponse {

    private String message;
    private Set<String> internalResources;
    private Set<String> externalResources;
    private Set<String> staticResources;
    private Set<String> otherResources;
}
