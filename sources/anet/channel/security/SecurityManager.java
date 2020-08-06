package anet.channel.security;

public class SecurityManager {
    private static volatile ISecurityFactory securityFactory = null;

    public static void setSecurityFactory(ISecurityFactory factory) {
        securityFactory = factory;
    }

    public static ISecurityFactory getSecurityFactory() {
        if (securityFactory == null) {
            securityFactory = new ISecurityFactory() {
                public ISecurity createSecurity(String authCode) {
                    return new SecurityGuardImpl(authCode);
                }

                public ISecurity createNonSecurity(String appSecret) {
                    return new NoSecurityGuardImpl(appSecret);
                }
            };
        }
        return securityFactory;
    }
}
