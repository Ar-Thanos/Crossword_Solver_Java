
public interface DictInterface
{
	/** Add a new String to the DictInterface
	 * @param s the string to be added
	 * @return true if the string was added successfully; false otherwise
	 */
	public boolean add(String s);



	/**
	 * @param s the string to be searched for
	 * @return 0 if s is not a word or prefix within the DictInterface
	 * 	       1 if s is a prefix within the DictInterface but not a
	 *                       valid word
	 *         2 if s is a word within the DictInterface but not a
	 *                        prefix to other words
	 *         3 if s is both a word within the DictInterface and a
	 *                        prefix to other words
	 */
	public int searchPrefix(StringBuilder s);

	
	public int searchPrefix(StringBuilder s, int start, int end);
}