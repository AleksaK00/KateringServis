//Metoda koja poziva kontroler za dodavanje artikla u korpu putem ajaxa
function dodajuKorpu(artikalId) {

    fetch('/korpa/dodaj', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                artikal: {
                    id: artikalId
                },
                kolicina: 1
            })
        })
        .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`Server error: ${text}`);
                    });

                }
                return response.json();
            }
        )
        .then(data => {
            if (data.uspeh) {
                if (data.brojArtikala) {
                    document.getElementById("brojArtikala").textContent = data.brojArtikala;
                }
                alert("Proizvod je uspešno dodat u korpu!");
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("Došlo je do greške prilikom dodavanja u korpu!")
        })
    ;
}