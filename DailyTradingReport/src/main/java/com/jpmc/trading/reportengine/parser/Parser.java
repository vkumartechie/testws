package com.jpmc.trading.reportengine.parser;

import java.util.List;

import com.jpmc.trading.reportengine.dto.Instruction;
import com.jpmc.trading.reportengine.exception.ReportingEngineException;

public interface Parser {

	public List<Instruction> parseInstructions() throws ReportingEngineException;
}
