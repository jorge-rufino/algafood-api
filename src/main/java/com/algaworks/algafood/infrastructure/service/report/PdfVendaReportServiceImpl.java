package com.algaworks.algafood.infrastructure.service.report;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.services.VendaQueryService;
import com.algaworks.algafood.domain.services.VendaReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PdfVendaReportServiceImpl implements VendaReportService{

	@Autowired
	private VendaQueryService vendaQueryService;
	
	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffSet) {
		
//	Utlizar o arquivo compilado do nosso modelo de relatorio do jasper. 
//	Este é a versão compilada do arquivo "vendas-diarias.jrxml" do Jasper
		
		try {
			var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");
			
			var parametros = new HashMap<String, Object>();
			
			//Setando o local para Brasil
			parametros.put("REPORT LOCALE", new Locale("pt", "BR"));
			
			var vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffSet);
			var datasource = new JRBeanCollectionDataSource(vendasDiarias);
			
			var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, datasource);
		
			return JasperExportManager.exportReportToPdf(jasperPrint);
			
		} catch (Exception e) {
			throw new ReporException("Não foi possível emitir relatório de vendas diarias.", e);
		}
	}

}
