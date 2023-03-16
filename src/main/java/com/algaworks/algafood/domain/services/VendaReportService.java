package com.algaworks.algafood.domain.services;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;

public interface VendaReportService {
	
	byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffSet);
}
