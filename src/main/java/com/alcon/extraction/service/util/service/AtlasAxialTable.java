package com.alcon.extraction.service.util.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alcon.extraction.dto.*;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.Relationship;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AtlasAxialTable {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public List<AxialTableModel> getTableDataFromTextractService(List<Block> blocks) {
		log.info("Enter getTableDataFromTextractService getTableDataFromTextractService(List<Block> blocks):::::");
		List<AxialTableModel> tables = null;
		Iterator<Block> blockIterator = blocks.iterator();
		try {

			if (blocks != null) {
				tables = new ArrayList<>();

				List<Block> tableBlocks = new ArrayList<>();
				Map<String, Block> blockMap = new HashMap<>();

				while (blockIterator.hasNext()) {
					Block block = blockIterator.next();
					if (block.getBlockType().equalsIgnoreCase("TABLE")) {
						tableBlocks.add(block);
					}
					blockMap.put(block.getId(), block);
				}
				for (Block blockModel : tableBlocks) {

					Map<Long, Map<Long, String>> rowMap = new HashMap<>();

					for (Relationship relationship : blockModel.getRelationships()) {
						if (relationship.getType().toString().equalsIgnoreCase("CHILD")) {

							for (String id : relationship.getIds()) {

								Block cell = blockMap.get(id);
								if (cell.getBlockType().toString().equalsIgnoreCase("CELL")) {

									long rowIndex = cell.getRowIndex();
									long columnIndex = cell.getColumnIndex();

									if (!rowMap.containsKey(rowIndex)) {
										rowMap.put(rowIndex, new HashMap<>());
									}

									Map<Long, String> columnMap = rowMap.get(rowIndex);
									columnMap.put(columnIndex, getCellText(cell, blockMap));
								}
							}
						}
					}
					AxialTableModel tm = new AxialTableModel(blockModel, rowMap);
					tables.add(tm);
				}
			}
		} catch (Exception e) {
			log.error("Could not get table from textract model", e.getLocalizedMessage());
		}
		log.info("Exit getTableDataFromTextractService getTableDataFromTextractService(List<Block> blocks):::::");
		return tables;
	}

	public String getCellText(Block cell, Map<String, Block> blockMap) {
		String text = "";
		try {

			if (cell != null) {

				for (Relationship relationship : cell.getRelationships()) {

					if (relationship.getType().toString().equalsIgnoreCase("CHILD")) {

						for (String id : relationship.getIds()) {

							Block word = blockMap.get(id);

							if (word.getBlockType().toString().equalsIgnoreCase("WORD")) {
								text += word.getText() + " ";

								// log.info("text=====>>>>" + text);
							} else if (word.getBlockType().toString().equalsIgnoreCase("SELECTION_ELEMENT")) {

								if (word.getSelectionStatus().toString().equalsIgnoreCase("SELECTED")) {
									text += "X ";
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("Exception ====" + e.getLocalizedMessage());
		}
		return text;
	}

}
