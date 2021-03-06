package me.prettyprint.cassandra.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.SuperColumn;

/**
 * Return type from get_range_slices for super columns
 * @author Ran Tavory
 *
 */
public final class OrderedSuperRows<SN,N,V> extends SuperRows<SN,N,V> {

  private final List<SuperRow<SN,N,V>> rowsList;

  public OrderedSuperRows(LinkedHashMap<String, List<SuperColumn>> thriftRet,
      Extractor<SN> sNameExtractor, Extractor<N> nameExtractor,
      Extractor<V> valueExtractor) {
    super(thriftRet, sNameExtractor, nameExtractor, valueExtractor);
    rowsList = new ArrayList<SuperRow<SN,N,V>>(thriftRet.size());
    for (Map.Entry<String, List<SuperColumn>> entry: thriftRet.entrySet()) {
      rowsList.add(new SuperRow<SN,N,V>(entry.getKey(), entry.getValue(), sNameExtractor,
          nameExtractor, valueExtractor));
    }
  }

  /**
   * Preserves rows order
   * @return an unmodifiable list of Rows
   */
  public List<SuperRow<SN,N,V>> getList() {
    return Collections.unmodifiableList(rowsList);
  }
}
