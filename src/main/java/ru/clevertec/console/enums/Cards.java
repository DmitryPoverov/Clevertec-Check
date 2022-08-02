package ru.clevertec.console.enums;

public enum Cards {
    CARD1("card-120"),
    CARD2("card-121"),
    CARD3("card-122"),
    CARD4("card-123");

    private final String number;

    Cards(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public static boolean isSuchCard(String card) {
        for (Cards c : Cards.values()) {
            if (card.equals(c.getNumber())) {
                return true;
            }
        }
        return false;
    }
}
