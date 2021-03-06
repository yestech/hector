package me.prettyprint.cassandra.model;

import static me.prettyprint.cassandra.model.HFactory.createColumnPath;
import me.prettyprint.cassandra.service.Keyspace;

// like a simple get operation
// may return a Column or a SuperColumn
public final class ColumnQuery<N,V> extends AbstractQuery<N,V,HColumn<N,V>> implements Query<HColumn<N,V>> {

  private String key;
  private N name;

  /*package*/ ColumnQuery(KeyspaceOperator keyspaceOperator, Extractor<N> nameExtractor,
      Extractor<V> valueExtractor) {
    super(keyspaceOperator, nameExtractor, valueExtractor);
  }

  public ColumnQuery<N,V> setKey(String key) {
    this.key = key;
    return this;
  }

  public ColumnQuery<N,V> setName(N name) {
    this.name = name;
    return this;
  }

  @Override
  public Result<HColumn<N, V>> execute() {
    return new Result<HColumn<N, V>>(keyspaceOperator.doExecute(
        new KeyspaceOperationCallback<HColumn<N, V>>() {
          @Override
          public HColumn<N, V> doInKeyspace(Keyspace ks) throws HectorException {
            try {
              org.apache.cassandra.thrift.Column thriftColumn =
                ks.getColumn(key, createColumnPath(columnFamilyName, name, columnNameExtractor));
              return new HColumn<N, V>(thriftColumn, columnNameExtractor, valueExtractor);
            } catch (NotFoundException e) {
              return null;
            }
          }
        }), this);
  }

  @Override
  public String toString() {
    return "ColumnQuery(" + key + "," + name + ")";
  }
}
