package org.example.javacw;

public class Horse {
    private String horseID;
    private String horseName;
    private String jockeyName;
    private int age;
    private int wins;
    private int compete;
    private String breed;
    private String group;
    private String imageFilePath;
    private double raceTime;
    private int place;

    // Horse constructor.
    public Horse(String horseID, String horseName, String jockeyName, int age, int wins, int compete, String breed, String group) {
        this.horseID = horseID;
        this.horseName = horseName;
        this.age = age;
        this.jockeyName = jockeyName;
        this.wins = wins;
        this.compete = compete;
        this.breed = breed;
        this.group = group;
    }

    // Getters
    public String getHorseID() {
        return horseID;
    }

    public String getHorseName() {
        return horseName;
    }

    public String getJockeyName() {
        return jockeyName;
    }

    public int getAge() {
        return age;
    }

    public int getWins() {
        return wins;
    }

    public int getCompete() {
        return compete;
    }

    public String getBreed() {
        return breed;
    }

    public String getGroup() {
        return group;
    }

    public double getRaceTime() {return raceTime;}
    public int getPlace() {return place;}



    // Setters
    public void setHorseID(String horseID) {
        this.horseID = horseID;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public void setJockeyName(String jockeyName) {
        this.jockeyName = jockeyName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setCompete(int compete) {
        this.compete = compete;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }
    public void setRaceTime(double raceTime) {this.raceTime = raceTime;}
    public void setPlace(int place) {this.place = place;}



    // Convert all data to String data type.
    // This method helps to store to text file.
    @Override
    public String toString() {
        return "ID: " + horseID + ", Name: " + horseName + ", Jockey: " + jockeyName +
                ", Age: " + age + ", Wins: " + wins + ", Compete: " + compete +
                ", Breed: " + breed + ", Group: " + group;
    }
}
