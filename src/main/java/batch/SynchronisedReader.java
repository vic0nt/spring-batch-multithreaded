package batch;

import org.springframework.batch.item.*;

public class SynchronisedReader implements ItemReader<Album>, ItemStream {

	private ItemReader<Album> delegate;
	@Override
	public void close() throws ItemStreamException {
		if (this.delegate instanceof ItemStream) {
            ((ItemStream)this.delegate).close();
		}
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (this.delegate instanceof ItemStream) {
            ((ItemStream)this.delegate).open(executionContext);
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		 if (this.delegate instanceof ItemStream) {
	            ((ItemStream)this.delegate).update(executionContext);
	    }
	}

	@Override
	public synchronized Album read() throws Exception, UnexpectedInputException,
            ParseException, NonTransientResourceException {
		return delegate.read();
	}

	public ItemReader<Album> getDelegate() {
		return delegate;
	}

	public void setDelegate(ItemReader<Album> delegate) {
		this.delegate = delegate;
	}
	
}
