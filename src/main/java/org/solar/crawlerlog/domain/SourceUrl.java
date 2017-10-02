package org.solar.crawlerlog.domain;

public class SourceUrl {

    private String url;

    private SourceUrl(String urlValue) {

        this.url = urlValue;
    }

    @Override
    public String toString(){
        return url;
    }


    public static SourceUrl fromString(String value) {

        return new SourceUrl(value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceUrl sourceUrl = (SourceUrl) o;

        return url.equals(sourceUrl.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

}
