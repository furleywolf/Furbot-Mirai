package cn.transfur.furbot;

/**
 * Date: 2022/1/27
 * Author: Jmeow
 */
public class Config {

    private Furbot furbot;

    public Furbot getFurbot() {
        return furbot;
    }

    public void setFurbot(Furbot furbot) {
        this.furbot = furbot;
    }

    public static class Furbot {

        private String prop1;

        private String prop2;

        public String getProp1() {
            return prop1;
        }

        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }

        public String getProp2() {
            return prop2;
        }

        public void setProp2(String prop2) {
            this.prop2 = prop2;
        }
    }

}