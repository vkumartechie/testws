package com.jpmc.trading.reportengine;

import com.jpmc.trading.reportengine.dto.TradeType;
import com.jpmc.trading.reportengine.exception.ReportingEngineException;
import com.jpmc.trading.reportengine.service.ReportEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ReportEngineApplication.
 */
public class ReportEngineApplication {

	/** The report engine service. */
	private static ReportEngineService reportEngineService;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ReportEngineApplication.class);

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws ReportingEngineException
	 */
	public static void main(final String[] args) throws ReportingEngineException {
		logger.info("Reporting eninge application...");
		try {
			reportEngineService = new ReportEngineService();
			if (args[0].equalsIgnoreCase("ISR")) {
				// Generate incoming settlement report
				reportEngineService.printDailyTradeAmounts(TradeType.SELL);
			} else if (args[0].equalsIgnoreCase("OSR")) {
				// Generate outgoing settlement report
				reportEngineService.printDailyTradeAmounts(TradeType.BUY);
			} else if (args[0].equalsIgnoreCase("IRR")) {
				// Generate incoming rankings report
				reportEngineService.printEntitiesRanking(TradeType.SELL);
			} else if (args[0].equalsIgnoreCase("ORR")) {
				// Generate outgoing rankings report
				reportEngineService.printEntitiesRanking(TradeType.BUY);
			} else {
				logger.error("Invalid option passed. Please check the usage");
			}
		} catch (ReportingEngineException e) {
			logger.error("ReportingEngineException while generating the report:", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception while generating the report:", e);
			throw new ReportingEngineException(e.getMessage(), e);
		}
	}
}
