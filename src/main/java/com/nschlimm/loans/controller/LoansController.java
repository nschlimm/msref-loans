/**
 * 
 */
package com.nschlimm.loans.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nschlimm.loans.config.LoansConfigService;
import com.nschlimm.loans.model.Customer;
import com.nschlimm.loans.model.Loans;
import com.nschlimm.loans.model.Properties;
import com.nschlimm.loans.repository.LoansRepository;

/**
 * @author Niklas Schlimm
 *
 */

@RestController
public class LoansController {

    
    private static final Logger log = LoggerFactory.getLogger(LoansController.class);

    @Autowired
    private LoansConfigService config;
    
    @Autowired
	private LoansRepository loansRepository;

	@PostMapping("/myLoans")
	public List<Loans> getLoansDetails(@RequestHeader("schlimmbank-correlation-id") String correlationId,@RequestBody Customer customer) {
        log.info("getLoansDetails() method started");
		List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
		log.info("getLoansDetails() method ended");
		if (loans != null) {
			return loans;
		} else {
			return null;
		}

	}

    @GetMapping("/loans/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(config.getMsg(), config.getBuildVersion(), config.getMailDetails(),
                config.getActiveBranches());
        String jsonStr = ow.writeValueAsString(properties);
        return jsonStr;
    }

}
