package org.barcode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        SearchTermsTopology topology = SearchTermsTopology.getInstance();
        topology.startStream();
    }
}