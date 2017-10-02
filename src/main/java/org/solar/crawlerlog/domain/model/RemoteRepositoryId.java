package org.solar.crawlerlog.domain.model;

public class RemoteRepositoryId {

    private String id;

    private RemoteRepositoryId(String repoId) {
        this.id = repoId;
    }


    public static RemoteRepositoryId fromString(String repoId) {
        return new RemoteRepositoryId(repoId);
    }


    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RemoteRepositoryId that = (RemoteRepositoryId) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
