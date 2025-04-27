package com.e_commerceapp.clothshops.dto;


//@Data
//@AllArgsConstructor
public class ImageDTO {
    private Long imageId;
    private String imageName;
    private String downloadUrl;

    public ImageDTO(Long imageId, String imageName, String downloadUrl) {
        this.imageId = imageId;
        this.imageName = imageName;
        this.downloadUrl = downloadUrl;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
