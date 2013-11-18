package gov.uscis.web.security;


public interface Permission {
    public boolean isAllowed(CustomUser user, Object obj);
}
