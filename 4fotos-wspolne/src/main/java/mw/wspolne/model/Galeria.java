package mw.wspolne.model;

import lombok.Getter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by mw on 24.01.16.
 */
@Getter
public class Galeria extends Zasob {
    public static String PLIK_OCEN = "oceny.xml";
    private List<Obrazek> obrazki;

    public Galeria(String etykieta, Path aSciezka) {
        super(etykieta, aSciezka);
    }

    public Galeria(Path sciezka) {
        super(sciezka);
    }

    public List<Obrazek> getObrazki() {
        if (obrazki == null) {
            obrazki = new ArrayList<Obrazek>();
        }
        return obrazki;
    }

    public List<Obrazek> filtrujWedlugKryteriow(KryteriaFiltrowania aKryteria) {
        return obrazki.stream().filter(podajWarunek(aKryteria)).collect(Collectors.toList());
    }

    private Predicate<Obrazek> podajWarunek(KryteriaFiltrowania aKryteria) {
        return new Predicate<Obrazek>() {
            @Override
            public boolean test(Obrazek obrazek) {
                if (aKryteria.getOperatorFiltrowaniaEnum() == null) {
                    return false;
                } else if (aKryteria.getOperatorFiltrowaniaEnum() == KryteriaFiltrowania.OperatorFiltrowaniaEnum.MNIEJSZE_ROWNE) {
                    return obrazek.getOcena() <= aKryteria.getOcena();
                }else if (aKryteria.getOperatorFiltrowaniaEnum() == KryteriaFiltrowania.OperatorFiltrowaniaEnum.ROWNE) {
                    return obrazek.getOcena() == aKryteria.getOcena();
                }else if (aKryteria.getOperatorFiltrowaniaEnum() == KryteriaFiltrowania.OperatorFiltrowaniaEnum.WIEKSZE_ROWNE) {
                    return obrazek.getOcena() >= aKryteria.getOcena();
                }

                return false;
            }
        };
    }
}
