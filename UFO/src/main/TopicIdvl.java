package main;

/**
 * 
 * @author Amit
 *
 */
public class TopicIdvl implements Comparable<TopicIdvl> {
	private int topicId;
	private Double topicDist;
	private String topicString;

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public Double getTopicDist() {
		return topicDist;
	}

	public void setTopicDist(Double topicDist) {
		this.topicDist = topicDist;
	}

	public String getTopicString() {
		return topicString;
	}

	public void setTopicString(String topicString) {
		this.topicString = topicString;
	}

	public int compareTo(TopicIdvl tp) {
		if (topicDist == tp.topicDist)
			return 0;
		else if (topicDist < tp.topicDist)
			return 1;
		else
			return -1;
	}
	
	public String toString() { 
	    return  this.topicId + "\t" + this.topicDist + "\t" + this.topicString + "'";
	} 

}
