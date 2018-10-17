package com.jpmc.trading.reportengine.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.jpmc.trading.reportengine.exception.ReportingEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpmc.trading.reportengine.dto.Instruction;

public class CSVParser implements Parser {
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(CSVParser.class);

	private Scanner scanner;

	private String line = null;

	private Integer index = 0;

	private Integer lineNumber = 0;

	private final List<Instruction> instructions = new ArrayList<>();

	private static final String DATE_FORMATTER_PATTERN = "MM-dd-yyyy";

	@Override
	public List<Instruction> parseInstructions() throws ReportingEngineException {
		this.getClass().getClassLoader();
		// open file input stream
		final BufferedReader reader = new BufferedReader(
				new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("data/trade_instructions.csv")));

		try {
			reader.readLine();// Skip the first line as it has headers

			while ((this.line = reader.readLine()) != null) {
				final Instruction instruction = new Instruction();
				this.scanner = new Scanner(this.line);
				this.scanner.useDelimiter(",");
				while (this.scanner.hasNext()) {
					final String data = this.scanner.next();
					if (null != data && (!"".equals(data))) {
						this.mapDataToInstruction(this.lineNumber, data, instruction);
						this.index++;
					}
				}
				this.index = 0;
				this.instructions.add(instruction);

				this.lineNumber++;
			}
			reader.close();
		} catch (final IOException e) {
			throw new ReportingEngineException(e.getMessage(), e);
		}

		return this.instructions;
	}

	private void mapDataToInstruction(final Integer lineNumber, final String data, final Instruction instruction)
			throws ReportingEngineException {
		LocalDate localDate;
		switch (this.index) {
		case 0:
			instruction.setEntity(data);
			break;
		case 1:
			instruction.setTradeType(data);
			break;
		case 2:
			instruction.setExchangeRate(Double.parseDouble(data));
			break;
		case 3:
			instruction.setCurrency(data);
			break;
		case 4:
			localDate = LocalDate.parse(data, DateTimeFormatter.ofPattern(CSVParser.DATE_FORMATTER_PATTERN));
			instruction.setInstructionDate(localDate);
			break;
		case 5:
			localDate = LocalDate.parse(data, DateTimeFormatter.ofPattern(CSVParser.DATE_FORMATTER_PATTERN));
			instruction.setSettlementDate(localDate);
			break;
		case 6:
			instruction.setUnits(Integer.parseInt(data));
			break;
		case 7:
			instruction.setPricePerUnit(Double.parseDouble(data));
			break;
		default:
			logger.error("invalid data::" + data);
			throw new ReportingEngineException("Invalid data found at line:" + lineNumber);
		}
	}

}
