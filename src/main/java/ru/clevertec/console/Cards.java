package ru.clevertec.console;

public enum Cards {
    CARD1("card-120"),
    CARD2("card-121"),
    CARD3("card-122"),
    CARD4("card-123");

    private String number;

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
