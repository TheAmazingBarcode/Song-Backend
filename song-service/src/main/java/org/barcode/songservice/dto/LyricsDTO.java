package org.barcode.songservice.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class LyricsDTO {
    private String id;
    private String name;
    private String lyrics;
    private String genre;
    private List<String> names;
}
