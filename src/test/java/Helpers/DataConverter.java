package Helpers;

public class DataConverter {
    public Enums.Browsers ConvertStringToBrowserKeyEnum(String browserString) {
        switch (browserString) {
            case "chrome":
                return Enums.Browsers.chrome;
            case "firefox":
                return Enums.Browsers.firefox;
            case "opera":
                return Enums.Browsers.opera;
        }

        Log.error("Incorrect browser string");
        return null;
    }
}
