package anet.channel.security;

public interface ISecurityFactory {
    ISecurity createNonSecurity(String str);

    ISecurity createSecurity(String str);
}
