package cn.transfur.furbot.base;

/**
 * Date: 2022/1/27
 * Author: Jmeow
 */
public class Config {

    public Config() {
        furbot = new Furbot();
    }

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

        private boolean showTail;

        private boolean respondFriends;

        private boolean respondGroups;

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

        public boolean isRespondFriends() {
            return respondFriends;
        }

        public void setRespondFriends(boolean respondFriends) {
            this.respondFriends = respondFriends;
        }

        public boolean isRespondGroups() {
            return respondGroups;
        }

        public void setRespondGroups(boolean respondGroups) {
            this.respondGroups = respondGroups;
        }

        public boolean isShowTail() {
            return showTail;
        }

        public void setShowTail(boolean showTail) {
            this.showTail = showTail;
        }
    }

}