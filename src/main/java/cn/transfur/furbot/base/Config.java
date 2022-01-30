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

        private boolean responseFriend;

        private boolean responseGroup;

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

        public boolean isResponseFriend() {
            return responseFriend;
        }

        public void setResponseFriend(boolean responseFriend) {
            this.responseFriend = responseFriend;
        }

        public boolean isResponseGroup() {
            return responseGroup;
        }

        public void setResponseGroup(boolean responseGroup) {
            this.responseGroup = responseGroup;
        }

        public boolean isShowTail() {
            return showTail;
        }

        public void setShowTail(boolean showTail) {
            this.showTail = showTail;
        }
    }

}