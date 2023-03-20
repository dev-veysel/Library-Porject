package com.library.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageSavedResponse extends SfResponse{

    private String imageID;

    public ImageSavedResponse(String message, boolean success, String imageID) {
        super(message, success);
        this.imageID = imageID;
    }
}