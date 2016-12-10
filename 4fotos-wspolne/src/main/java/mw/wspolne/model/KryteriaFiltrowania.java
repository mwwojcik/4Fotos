package mw.wspolne.model;

/**
 * Created by mw on 28.01.16.
 */
public class KryteriaFiltrowania {
    private double ocena=5;
    private OperatorFiltrowaniaEnum rodzajKryteriow = OperatorFiltrowaniaEnum.MNIEJSZE_ROWNE;

    public double getOcena() {
        return ocena;
    }

    public void setOcena(double ocena) {
        this.ocena = ocena;
    }


    public void setOperatorFiltrowaniaEnum(OperatorFiltrowaniaEnum rodzajKryteriow) {
        this.rodzajKryteriow = rodzajKryteriow;
    }

    public OperatorFiltrowaniaEnum getOperatorFiltrowaniaEnum() {
        return rodzajKryteriow;
    }

    public enum OperatorFiltrowaniaEnum {
        MNIEJSZE_ROWNE,
        ROWNE,
        WIEKSZE_ROWNE;
    }
}
