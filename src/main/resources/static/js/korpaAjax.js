//Event listener za iskakanje modala za odabir kolicine i potvrdu dodavanja u korpu
document.addEventListener('DOMContentLoaded', function() {
    const korpaModal = document.getElementById('dodajuKorpuModal');
    let trenutniArtikalId;

    //Kada se modal prikaze, sacuvaj id trenutnog artikla, i njegov naziv za ispis u naslovu modala
    korpaModal.addEventListener('show.bs.modal', function(event) {
        const button = event.relatedTarget;
        trenutniArtikalId = button.getAttribute('data-artikal-id');

        korpaModal.querySelector('.modal-title').textContent = button.getAttribute('data-artikal-naziv');
        korpaModal.querySelector('#slikaArtikla').src = "/images/products/" + trenutniArtikalId + ".jpg";
    });

    //Pozivanje funkcije dodajUKorpu kada se potvrdi narucivanje
    document.getElementById('potvrda').addEventListener('click', function() {
        const kolicina = document.getElementById('kolicina').value;
        dodajuKorpu(trenutniArtikalId, kolicina);

        //Zatvaranje modala na kraju akcije
        const modal = bootstrap.Modal.getInstance(korpaModal);
        modal.hide();
        document.getElementById('kolicina').value = 1;
    });
});


//Metoda koja poziva kontroler za dodavanje artikla u korpu putem ajaxa
function dodajuKorpu(artikalId, brArtikala) {

    fetch('/korpa/dodaj', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                artikal: {
                    id: artikalId
                },
                kolicina: brArtikala
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