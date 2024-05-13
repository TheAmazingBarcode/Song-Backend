package org.barcode.lyricsservice.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
   String field;
   Object value;
   String type;
}
