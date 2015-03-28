//
//  MatchesViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-02-12.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit

class MatchesViewController: ViewController {
    
    @IBOutlet weak var userLabel: UILabel!
    @IBOutlet weak var tweet1Label: UILabel!
    @IBOutlet weak var tweet2Label: UILabel!
    @IBOutlet weak var tweet3Label: UILabel!
    @IBOutlet weak var tweet4Label: UILabel!
    @IBOutlet weak var tweet5Label: UILabel!
    
    var potentialList: [PreparedPotentialMatch] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        self.navigationController?.setToolbarHidden(false, animated: false)
        self.navigationController?.toolbar.barTintColor = UIColor(red: CGFloat(39.0/255.0), green: CGFloat(174.0/255.0), blue: CGFloat(96.0/255.0), alpha: CGFloat(1.0))
        self.navigationController?.toolbar.tintColor = UIColor.whiteColor()
        
        loadPotentialMatch()
    }
    
    override func viewWillDisappear(animated: Bool) {
        self.navigationController?.setToolbarHidden(true, animated: false)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // Decline to be matched with the user via button or swipe
    func dismissMatch() {
        // send that you rejected them
        Curried().reject(token: xAuthToken!, username: potentialList[0].username)
        
        // load next potential match
        potentialList.removeAtIndex(0)
        loadPotentialMatch()
    }
    
    // Accept the match via button or swipe
    func acceptMatch() {
        // follow that user
        // send that you matched with them
        Curried().accept(token: xAuthToken!, username: potentialList[0].username)
        
        // load next potential match
        potentialList.removeAtIndex(0)
        loadPotentialMatch()
    }
    
    private func loadPotentialMatch() {
        if potentialList.count == 0 {
            if let tkn = xAuthToken {
                Curried().getPotentialMatches(token: tkn, callback: potentialMatchesLoadedCallback)
            }
        }
    }
    
    private func potentialMatchesLoadedCallback(res: [PreparedPotentialMatch]?) {
        if let tempList = res {
            potentialList = tempList
            
            if(potentialList.count > 0) {
                let potentialMatch = potentialList[0]
                userLabel.text = potentialMatch.username
                
                if potentialMatch.tweets.count >= 1 { tweet1Label.text = potentialMatch.tweets[0] } else { tweet1Label.text = "" }
                if potentialMatch.tweets.count >= 2 { tweet2Label.text = potentialMatch.tweets[1] } else { tweet2Label.text = "" }
                if potentialMatch.tweets.count >= 3 { tweet3Label.text = potentialMatch.tweets[2] } else { tweet3Label.text = "" }
                if potentialMatch.tweets.count >= 4 { tweet4Label.text = potentialMatch.tweets[3] } else { tweet4Label.text = "" }
                if potentialMatch.tweets.count >= 5 { tweet5Label.text = potentialMatch.tweets[4] } else { tweet5Label.text = "" }
                
            } else {
                userLabel.text   = ""
                tweet1Label.text = ""
                tweet2Label.text = "We have no more potential matches at this time."
                tweet3Label.text = ""
                tweet4Label.text = ""
                tweet5Label.text = ""
                
            }

        }
    }
    
    @IBAction func noButton(sender: UIBarButtonItem) {
        dismissMatch()
    }

    @IBAction func yesButton(sender: UIBarButtonItem) {
        acceptMatch()
    }
    
    // Swipe to the right (start left, move right)
    @IBAction func noSwipe(sender: UISwipeGestureRecognizer) {
        dismissMatch()
    }
    
    // Swipe to the left (start right, move left)
    @IBAction func yesSwipe(sender: UISwipeGestureRecognizer) {
        acceptMatch()
    }
}
