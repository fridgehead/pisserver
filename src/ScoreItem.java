import java.awt.image.BufferedImage;


	public class ScoreItem implements Comparable<ScoreItem>{
		public int score = 0;
		public BufferedImage scoreImage, faceImage;
		public boolean newScore = false;
		public String location = "";
		public String name = "";
		public long networkId = -1;

		public ScoreItem(String name, int score, String loc){
			this.score = score;
			this.name = name;	
			location = loc;
		}

		public int compareTo(ScoreItem o) {

			ScoreItem a = (ScoreItem)o;
			if(a.score > score){
				return 1;
			} else if (a.score < score){
				return -1;

			}else {
				return 0;
			}


		}

	}