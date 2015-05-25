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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (artworkUrl != null ? !artworkUrl.equals(track.artworkUrl) : track.artworkUrl != null) return false;
        if (duration != null ? !duration.equals(track.duration) : track.duration != null) return false;
        if (likesCount != null ? !likesCount.equals(track.likesCount) : track.likesCount != null) return false;
        if (streamUrl != null ? !streamUrl.equals(track.streamUrl) : track.streamUrl != null) return false;
        if (title != null ? !title.equals(track.title) : track.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (likesCount != null ? likesCount.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (artworkUrl != null ? artworkUrl.hashCode() : 0);
        result = 31 * result + (streamUrl != null ? streamUrl.hashCode() : 0);
        return result;
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