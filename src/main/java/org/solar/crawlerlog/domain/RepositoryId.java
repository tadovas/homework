package org.solar.crawlerlog.domain;

public class RepositoryId {

    private String id;

    private RepositoryId(String repoId) {
        this.id = repoId;
    }


    public static RepositoryId newId(String repoId) {
        return new RepositoryId(repoId);
    }


    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepositoryId that = (RepositoryId) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
