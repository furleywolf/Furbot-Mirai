package cn.transfur.furbot.base;

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

        private String qq;

        private String authKey;

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getAuthKey() {
            return authKey;
        }

        public void setAuthKey(String authKey) {
            this.authKey = authKey;
        }
    }

}