
package lol.search;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This page is what the user will see when he/she starts the application. 
 * @author Oscar
 */
public class MainPage implements ActionListener{
    private JFrame masterFrame;
    //class objects
    private GameStaticData objGameStaticData; //class object to retrieve information from GameStaticData
    private LoLSearch objLoLSearch = new LoLSearch(); //class object to retrieve api key
    //general variables
    private JLabel masterLabel; //holds the background image and masterPanel
    private JPanel masterPanel; //holds swing components of the frame
    private final JTextField summonerTextField = new JTextField(20);; //textfield for the user input
    private JComboBox regionsComboBox; //regions combobox, will be initialized in method
    private JButton searchButton; //button 
    private JLabel errorLabel;
    private final Timer frameTimer;
    //end values
    private String nameInput; //user input, name to be looked up
    private String regionCodeValue; //region code of region selected
    private String version;
    
    public MainPage(JFrame mainFrame, String name){ //arg constructor
        System.out.println("Loading page...");
        nameInput = name;
        frameTimer = new Timer();
        frameTimer.schedule(new TimerTask(){
            @Override
            public void run(){
                masterFrame.revalidate();
                masterFrame.repaint();
            }
        }, 17,17);
        this.masterFrame = mainFrame;
        loadFont();
        initializeMasterLabel(this.masterFrame);
        initializeMasterPanel(this.masterFrame, this.masterLabel);
        
        this.masterFrame.revalidate();
        System.out.println("Done.");
    }
    
    /*Set background of the frame and prepare for the main panel*/
    private void initializeMasterLabel(JFrame frame){
        
        //GameStaticData class object
        objGameStaticData = new GameStaticData();
        //background label
        this.masterLabel = new JLabel(objGameStaticData.getBackgroundImageIcon()); //use method from obj to get a background
        this.masterLabel.setLayout(new FlowLayout());
        this.masterLabel.setOpaque(false);
        frame.add(this.masterLabel);
    }
    
    /***Master panel methods*************************************************************************************************/
    /*Set the master panel that will hold all swing components in the frame for the main page*/
    private void initializeMasterPanel(JFrame frame, JLabel label){
        
        this.masterPanel = new JPanel();
        this.masterPanel.setLayout(new BoxLayout(this.masterPanel, BoxLayout.Y_AXIS));
        this.masterPanel.setBackground(new Color(0,0,0,150));
        this.masterPanel.setBorder(BorderFactory.createEmptyBorder(0,0,243, 0)); //border
        
            /*add components to the masterPanel*/
            JPanel spacer = new JPanel();
            spacer.setOpaque(false);
            spacer.setPreferredSize(new Dimension(50,30));
            addLogo(this.masterPanel);
            this.masterPanel.add(spacer);
            addSummonerLabel(this.masterPanel);
            addSummonerTextField(this.masterPanel);
            addHistoryButton(frame, this.masterPanel);
            addRankedStatsButton(frame, this.masterPanel);
            addRegionsComboBox(this.masterPanel);
            addErrorPanel();
            
        label.add(this.masterPanel);
    }
    
    private void addErrorPanel(){
        //summmoner label
        this.errorLabel = new JLabel();
        this.errorLabel.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 14)); //custom font
        this.errorLabel.setForeground(Color.RED); //text color
        JPanel labelPanel = new JPanel();
        labelPanel.add(this.errorLabel);
        JPanel labelHolder = new JPanel();
        labelHolder.add(labelPanel);
        labelHolder.setOpaque(false);
        labelPanel.setBackground(new Color(0,0,0,0));
        this.masterPanel.add(labelPanel);
    }
    
    /*Add logo to the master panel*/
    private void addLogo(JPanel panel){
        try {
            URL url = new URL("https://p2.zdassets.com/hc/theme_assets/43400/200033224/league-logo.png");
            BufferedImage c2 = ImageIO.read(url);
            ImageIcon leagueLogo = new ImageIcon(c2);
            Image logoImage = leagueLogo.getImage();
            ImageIcon newIcon = new ImageIcon(logoImage.getScaledInstance(450, 178, Image.SCALE_SMOOTH)); //resize
            JLabel logo = new JLabel(newIcon);
            JPanel logoHolder = new JPanel();
            logoHolder.add(logo);
            logo.setHorizontalAlignment(SwingConstants.CENTER);
            logo.setBorder(BorderFactory.createEmptyBorder(0,0, 0, 0)); //border
            logoHolder.setOpaque(false);
            panel.add(logoHolder);
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*Add summoner label to the master panel*/
    private void addSummonerLabel(JPanel panel){
        //summmoner label
        JLabel label1 = new JLabel("SUMMONER:");
        label1.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 32)); //custom font
        label1.setForeground(Color.WHITE); //text color
        JPanel labelPanel = new JPanel();
        labelPanel.add(label1);
        JPanel labelHolder = new JPanel();
        labelHolder.add(labelPanel);
        labelHolder.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); //border
        labelHolder.setOpaque(false);
        labelPanel.setBackground(new Color(0,0,0,0));
        panel.add(labelPanel);
    }
    
    /*Add input textfield to the master panel*/
    private void addSummonerTextField(JPanel panel){
        //textfield
        JPanel textFieldHolder = new JPanel();
        this.summonerTextField.setForeground(Color.BLACK);
        this.summonerTextField.setBackground(Color.LIGHT_GRAY);
        this.summonerTextField.setHorizontalAlignment(SwingConstants.CENTER);
        this.summonerTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.summonerTextField.setText(nameInput);
        this.summonerTextField.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 14)); //custom font
        this.summonerTextField.setPreferredSize(new Dimension(222,30));
        this.summonerTextField.addActionListener(this);
        textFieldHolder.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0)); //border
        textFieldHolder.add(this.summonerTextField);
        textFieldHolder.setOpaque(false);
        panel.add(textFieldHolder);
    }
    
    public void actionPerformed(ActionEvent e) { //action event for when either the button or the enter key is pressed
        nameInput = summonerTextField.getText().toLowerCase().replaceAll(" ", ""); //change text format for URL
        if(nameInput.equals("")){
            errorLabel.setText("You need to type something!");
        }
        else{
            errorLabel.setText("Loading...");
            String comboBoxValue = getComboBoxValue(regionsComboBox).toString(); //get combobox string
            ConvertToCountryCode(comboBoxValue); //convert it to country code ex. na, eu, ru, etc.
            getMostRecentVersion(regionCodeValue);
            System.out.println("Loading next page...");
            //System.out.println("The button was pressed.\nnameInput text: " + nameInput + "\nRegion code: " + regionCodeValue+"\nVersion: " + version);
            /*This is where the next page will be called. JSON information must be retrieved from another class.*/
            //class objects
            Summoner_ByName objSummByName = new Summoner_ByName(nameInput, regionCodeValue, version); //get Summoner_ByName information from endpoint
            if(objSummByName.getDoesExist()){ //if the searched summoner exists
                //prepare frame for next page
                masterFrame.getContentPane().removeAll();
                masterFrame.revalidate();
                masterFrame.repaint();
                Game_ById GAME_BY_ID = new Game_ById(objSummByName.getSummonerId(), regionCodeValue, version); //get Game_ById information from endpoint
                LoLStaticData_AllChampions DATA_ALL_CHAMPIONS = new LoLStaticData_AllChampions(GAME_BY_ID.getChampionIdList(), regionCodeValue, version); //get data for all champions from endpoint
                League_ById LEAGUE_BY_ID = new League_ById(regionCodeValue, objSummByName.getSummonerId());
                MatchHistoryPage MATCH_HISTORY_PAGE = new MatchHistoryPage(regionCodeValue, masterFrame, objSummByName, GAME_BY_ID, DATA_ALL_CHAMPIONS, LEAGUE_BY_ID); //proceed to match history page 
                frameTimer.cancel(); //stop frame timer from going forever
            }
            else{
                errorLabel.setText("Player does not exist. Please try again.");
            }    
        }
    }
    
    /*Add button to master panel*/
    private void addHistoryButton(JFrame frame, JPanel panel){
        ImageIcon buttonImage = new ImageIcon("assets\\other\\button.png");
        ImageIcon buttonPressedImage = new ImageIcon("assets\\other\\buttonPressed.png");
        //button
        JPanel buttonHolder = new JPanel();
        this.searchButton = new JButton("MATCH HISTORY");
        this.searchButton.setPreferredSize(new Dimension(222,50));
        this.searchButton.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 10)); //custom font
        this.searchButton.setForeground(Color.WHITE); //text color
        this.searchButton.setBackground(Color.DARK_GRAY);
        this.searchButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.searchButton.setHorizontalTextPosition(AbstractButton.CENTER);
        //search button background image
        //regular button
        Image tempImage = buttonImage.getImage();
        Image newTempImg = tempImage.getScaledInstance(222, 50, Image.SCALE_SMOOTH);
        buttonImage = new ImageIcon(newTempImg);
        //pressed button
        Image tempImage2 = buttonPressedImage.getImage();
        Image newTempImg2 = tempImage2.getScaledInstance(222, 50, Image.SCALE_SMOOTH);
        buttonPressedImage = new ImageIcon(newTempImg2);
        this.searchButton.setIcon(buttonImage);
        this.searchButton.setRolloverIcon(buttonPressedImage);
        
        this.searchButton.addActionListener(this);
        buttonHolder.add(this.searchButton);
        buttonHolder.setOpaque(false);
        panel.add(buttonHolder);
    }
    
    /*Add button to master panel*/
    private void addRankedStatsButton(JFrame frame, JPanel panel){
        ImageIcon buttonImage = new ImageIcon("assets\\other\\button.png");
        ImageIcon buttonPressedImage = new ImageIcon("assets\\other\\buttonPressed.png");
        //button
        JButton rankedButton;
        JPanel buttonHolder = new JPanel();
        rankedButton = new JButton("RANKED STATS");
        rankedButton.setPreferredSize(new Dimension(222,50));
        rankedButton.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 10)); //custom font
        rankedButton.setForeground(Color.WHITE); //text color
        rankedButton.setBackground(Color.DARK_GRAY);
        rankedButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        rankedButton.setHorizontalTextPosition(AbstractButton.CENTER);
        //search button background image
        //regular button
        Image tempImage = buttonImage.getImage();
        Image newTempImg = tempImage.getScaledInstance(222, 50, Image.SCALE_SMOOTH);
        buttonImage = new ImageIcon(newTempImg);
        //pressed button
        Image tempImage2 = buttonPressedImage.getImage();
        Image newTempImg2 = tempImage2.getScaledInstance(222, 50, Image.SCALE_SMOOTH);
        buttonPressedImage = new ImageIcon(newTempImg2);
        rankedButton.setIcon(buttonImage);
        rankedButton.setRolloverIcon(buttonPressedImage);
        
        rankedButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){ //button pressed
                        System.out.println("Ranked Stats button pressed.");
                        nameInput = summonerTextField.getText().toLowerCase().replaceAll(" ", ""); //change text format for use with URL
                        
                        if(nameInput.equals("")){
                            errorLabel.setText("You need to type something!");
                        }
                        else{
                            String comboBoxValue = getComboBoxValue(regionsComboBox).toString(); //get combobox string
                            ConvertToCountryCode(comboBoxValue); //convert it to country code ex. na, eu, ru, etc.
                            getMostRecentVersion(regionCodeValue);
                            Summoner_ByName objSummByName = new Summoner_ByName(nameInput, regionCodeValue, version); //get Summoner_ByName information from endpoint
                            RankedStatsPage RANKED_STATS_PAGE_TEST = new RankedStatsPage(version, regionCodeValue, objSummByName);
                            System.out.println("Player ranked status: " + RANKED_STATS_PAGE_TEST.isExist());
                            if(objSummByName.getDoesExist()){
                                if(RANKED_STATS_PAGE_TEST.isExist()){
                                    //printValues();
                                    masterFrame.getContentPane().removeAll();
                                    RankedStatsPage RANKED_STATS_PAGE = new RankedStatsPage(version, masterFrame, regionCodeValue, objSummByName);
                                }
                                else{
                                    errorLabel.setText("Ranked history for this player does not exist.");
                                }  
                            }
                            else{
                                errorLabel.setText("Player does not exist. Please try again.");
                            }
                        }
                        
                    }
                });
        buttonHolder.add(rankedButton);
        buttonHolder.setOpaque(false);
        panel.add(buttonHolder);
    }
    
    public void printValues(){
        System.out.println("Input Values from Main Page:\n"
                            +  "    Input: " + nameInput + "\n"
                            +  "    Region Code: " + regionCodeValue + "\n"
                            +  "    Most Recent Version: " + version + "\n");
    }
    /*Get selected value from combobox*/
    private Object getComboBoxValue(JComboBox cb){
        return cb.getSelectedItem();
    }
    
    private void ConvertToCountryCode(String country){
        switch (country) {
            case "North America":
                this.regionCodeValue = "na";
                break;
            case "Brazil":
                this.regionCodeValue = "br";
                break;
            case "EU Nordic & East":
                this.regionCodeValue = "eune";
                break;
            case "EU West":
                this.regionCodeValue = "euw";
                break;
            case "Korea":
                this.regionCodeValue = "kr";
                break;
            case "Latin America North":
                this.regionCodeValue = "lan";
                break;
            case "Latin America South":
                this.regionCodeValue = "las";
                break;
            case "Oceania":
                this.regionCodeValue = "oce";
                break;
            case "Public Beta Environment":
                this.regionCodeValue = "pbe";
                break;
            case "Russia":
                this.regionCodeValue = "ru";
                break;
            case "Turkey":
                this.regionCodeValue = "tr";
                break;
        }
    }
    
    /*Add combobox to the master panel for the different regions*/
    private void addRegionsComboBox(JPanel panel){
        //combobox
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setOpaque(false);
        this.regionsComboBox = new JComboBox(this.objGameStaticData.getRegionsArray()); //different regions in the combo box
        this.regionsComboBox.setEditable(false); //allow the user to choose
        this.regionsComboBox.setFont(new Font("Sen-Regular", Font.CENTER_BASELINE, 10)); //custom font
        this.regionsComboBox.setForeground(Color.WHITE);
        this.regionsComboBox.setBackground(new Color(0,0,0,100));
        this.regionsComboBox.setOpaque(false);
        this.regionsComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JComboBox comboBox = (JComboBox) e.getSource();
                Object selected = comboBox.getSelectedItem();
            }
        });
        comboBoxPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0)); //border
        comboBoxPanel.add(this.regionsComboBox);
        comboBoxPanel.setOpaque(false);
        panel.add(comboBoxPanel);
    }
    
    /*Load font for use with labels*/
    private void loadFont(){
        //load font
        try {
            //create the font to use
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets\\fonts\\Sen-Regular.ttf")));
        }catch (IOException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FontFormatException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getMostRecentVersion(String regionCode){
        String jsonResponse = null;
        try {
            URL url = new URL("https://global.api.pvp.net/api/lol/static-data/"+regionCode+"/v1.2/versions?api_key=" + this.objLoLSearch.getApiKey());
            //retrieve JSON
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    jsonResponse = strTemp;
            }
            JSONArray obj = new JSONArray(jsonResponse);
            
            this.version = obj.get(0).toString();
        } catch (JSONException ex) {
            Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameStaticData.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
