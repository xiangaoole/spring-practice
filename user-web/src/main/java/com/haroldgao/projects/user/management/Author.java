package com.haroldgao.projects.user.management;

/**
 *
 */
public final class Author implements AuthorMXBean {
    private volatile static Author sTheOne;
    private volatile String name;
    private volatile String email;
    private volatile String github;
    private volatile Hobbies hobbies;

    public static Author getInstance() {
        if (sTheOne == null) {
            synchronized (Author.class) {
                if (sTheOne == null) {
                    sTheOne = new Author();
                    sTheOne.setEmail("harold@github.com");
                    sTheOne.setGithub("https://github.com/xiangaoole");
                    sTheOne.setName("Harold Gao");
                    Hobbies hobbies = new Hobbies();
                    hobbies.setMovie("Nomadland");
                    hobbies.setMusic("Johann Sebastian Bach");
                    sTheOne.setHobbies(hobbies);
                }
            }
        }
        return sTheOne;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public Hobbies getHobbies() {
        return hobbies;
    }

    public void setHobbies(Hobbies hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", github='" + github + '\'' +
                ", hobbies=" + hobbies +
                '}';
    }

    public static class Hobbies {
        private volatile String music;
        private volatile String movie;

        public void setMusic(String music) {
            this.music = music;
        }

        public void setMovie(String movie) {
            this.movie = movie;
        }

        public Hobbies() {
        }

        public String getMusic() {
            return music;
        }

        public String getMovie() {
            return movie;
        }

        @Override
        public String toString() {
            return "Hobbies{" +
                    "music='" + music + '\'' +
                    ", movie='" + movie + '\'' +
                    '}';
        }
    }
}
