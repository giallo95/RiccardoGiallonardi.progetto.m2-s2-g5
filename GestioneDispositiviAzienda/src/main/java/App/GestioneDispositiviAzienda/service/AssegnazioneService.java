package App.GestioneDispositiviAzienda.service;

import App.GestioneDispositiviAzienda.entities.Dipendente;
import App.GestioneDispositiviAzienda.entities.Dispositivo;
import App.GestioneDispositiviAzienda.entities.Stato;
import App.GestioneDispositiviAzienda.exception.ResourceNotFoundException;
import App.GestioneDispositiviAzienda.repository.DipendenteRepository;
import App.GestioneDispositiviAzienda.repository.DispositivoRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssegnazioneService {
    @Autowired
    private DipendenteRepository dipendenteRepository;
    @Autowired
    private DispositivoRepository dispositivoRepository;

    public Dispositivo assegnaDispositivo(Long dipendenteId, Long dispositivoId) throws BadRequestException {
        Dipendente dipendente = dipendenteRepository.findById(dipendenteId)
                .orElseThrow(() -> new ResourceNotFoundException("Dipendente non trovato"));
        Dispositivo dispositivo = dispositivoRepository.findById(dispositivoId)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo non trovato"));

        if (!"DISPONIBILE".equalsIgnoreCase(String.valueOf((dispositivo.getStato())))) {
            throw new BadRequestException("Dispositivo non disponibile per l'assegnazione");
        }

        dispositivo.setStato(Stato.valueOf("ASSEGNATO"));
        dispositivo.setDipendente(dipendente); // Associa il dispositivo al dipendente
        return dispositivoRepository.save(dispositivo);
    }
}
