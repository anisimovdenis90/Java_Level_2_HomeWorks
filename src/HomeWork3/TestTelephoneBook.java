package HomeWork3;

public class TestTelephoneBook {
    public static void main(String[] args) {
        TelephoneBook telephoneBook = new TelephoneBook("89993336551", "Ефимов");
        telephoneBook.add("87771112233", "Деньков");
        telephoneBook.add("79992232233", "Антонов");
        telephoneBook.add("86661112244", "Деньков");
        telephoneBook.add("85551112000", "Ефимов");
        telephoneBook.add("84441115533", "Деньков");
        telephoneBook.add("83330002233", "Меньшиков");

        telephoneBook.print();
        telephoneBook.get("Ефимов");
    }


}
