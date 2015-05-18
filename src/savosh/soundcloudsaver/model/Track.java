package savosh.soundcloudsaver.model;


public class Track {

    String title;
    Integer likesCount;
    Long duration;
    String artworkUrl;
    String streamUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    @Override
    public String toString() {
        return "Track{" +
                "title='" + title + '\'' +
                ", likesCount=" + likesCount +
                ", duration=" + duration +
                ", artworkUrl='" + artworkUrl + '\'' +
                ", streamUrl='" + streamUrl + '\'' +
                '}';
    }
}
