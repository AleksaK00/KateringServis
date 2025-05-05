//Funkcija za ispis cene prilikom promene broja osoba
function IzmeniCenu() {

    //Preuzimanje podataka
    const brOsoba = document.getElementById("brOsoba").value;
    const cena = parseFloat(document.getElementById("cena").dataset.cena);

    //Izracunavanje, dodavanje popusta ako ima vise od 100 osoba
    let ukupnaCena = brOsoba * cena;
    if (brOsoba >= 100) {
        ukupnaCena *= 0.9;
    }

    //Formatiranje valute
    const formatiranaUkupnaCena = new Intl.NumberFormat('sr-RS', {
        style: 'currency',
        currency: 'RSD'
    }).format(ukupnaCena);

    //Ispis
    document.getElementById("cena").textContent = formatiranaUkupnaCena;
}