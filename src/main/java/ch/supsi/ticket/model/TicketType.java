package ch.supsi.ticket.model;

public enum TicketType {
    BUG("Bug"),
    FEATURE_REQUEST("Richiesta Funzionalità"),
    SUPPORT("Supporto"),
    MAINTENANCE("Manutenzione");

    private final String displayName;

    TicketType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}