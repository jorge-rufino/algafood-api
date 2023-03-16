package com.algaworks.algafood.infrastructure.service.report;

import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.services.VendaReportService;

@Service
public class PdfVendaReportServiceImpl implements VendaReportService{

	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffSet) {

		return null;
	}

}
