//
//  MessageViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-03-26.
//  Copyright (c) 2015 Group1. All rights reserved.
//
import JSQMessagesViewController

class MessageViewController: JSQMessagesViewController, UIActionSheetDelegate {
    
    var messageHandler: MessageHandler! //Allows incoming messages
        
    var messages = [Message]() //You have to append here, all the messages, that you are sending.
    
    /*
    *   Here you set up the color for each of the incoming and outgoing bubble.
    *   Grab the color from the provided jsq color factory.
    */
    var outgoingBubbleImageView = JSQMessagesBubbleImageFactory().outgoingMessagesBubbleImageWithColor(UIColor.jsq_messageBubbleGreenColor())
    var incomingBubbleImageView = JSQMessagesBubbleImageFactory().incomingMessagesBubbleImageWithColor(UIColor.jsq_messageBubbleLightGrayColor())
    
    var batchMessages = true
    
    /*
    *   You can send the message from here.
    */
    func SendMessage(text: String!, sender: String!) {
        let message = Message(sender: sender, text: text)
        messages.append(message)
        
        // Send to server
        if let tkn = xAuthToken {
            Curried().sendMessage(obj: MatchMessage(matchID: converseWith.matchID, sender: user, message: text, dateTime: NSDate()), token: tkn)
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = converseWith.username
        automaticallyScrollsToMostRecentMessage = true
        inputToolbar.contentView.leftBarButtonItem = nil
        //self.showLoadEarlierMessagesHeader = true
        
        senderDisplayName = user
        senderId = senderDisplayName
        
        messageHandler = MessageHandler(matchID: converseWith.matchID, handler: receivedMessageCallback)
        messageHandler.start()
        
        // Load previous messages from the server
        if let tkn = xAuthToken {
            Curried().getMessages(token: tkn, matchID: converseWith.matchID, callback: loadServerMessagesCallback)
        }
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        //Enable the springy bubbles from here.
        collectionView.collectionViewLayout.springinessEnabled = true
    }
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    // MARK: - ACTIONS
    
    func receivedMessagePressed(sender: UIBarButtonItem) {
        // Simulate reciving message
        showTypingIndicator = !showTypingIndicator
        scrollToBottomAnimated(true)
    }
    
    /*
    *   The real magic happens here. This is the method which is called when you press the send button.
    *   here, you could play a nice little song along with calling the appropiate methids in order.
    */
    override func didPressSendButton(button: UIButton!, withMessageText text: String!, senderId: String!, senderDisplayName: String!, date: NSDate!) {
        JSQSystemSoundPlayer.jsq_playMessageSentSound()
        SendMessage(text, sender: senderDisplayName)
        finishSendingMessageAnimated(true)
    }
    
    // Here, you can tell the controller about the data belonging to which bubble.
    override func collectionView(collectionView: JSQMessagesCollectionView!, messageDataForItemAtIndexPath indexPath: NSIndexPath!) -> JSQMessageData! {
        return messages[indexPath.item]
    }
    
    // Differentiate the incomming and outgoing messages, and set their bubble color accordingly
    override func collectionView(collectionView: JSQMessagesCollectionView!, messageBubbleImageDataForItemAtIndexPath indexPath: NSIndexPath!) -> JSQMessageBubbleImageDataSource! {
        let message = messages[indexPath.item]
        
        if message.senderId() == senderDisplayName {
            return outgoingBubbleImageView
        }
        return incomingBubbleImageView
    }
    
    // Set the avatar image for the incomming and outgoing messages
    override func collectionView(collectionView: JSQMessagesCollectionView!, avatarImageDataForItemAtIndexPath indexPath: NSIndexPath!) -> JSQMessageAvatarImageDataSource! {
        return nil
    }
    
    override func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return messages.count
    }
    
    // Here, you can configure the heck out of an individual cell.
    override func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        let cell = super.collectionView(collectionView, cellForItemAtIndexPath: indexPath) as JSQMessagesCollectionViewCell
        
        let message = messages[indexPath.item]
        if message.senderId() == senderDisplayName {
            cell.textView.textColor = UIColor.whiteColor()
        } else {
            cell.textView.textColor = UIColor.blackColor()
        }
        
        let attributes : [NSObject:AnyObject] = [NSForegroundColorAttributeName:cell.textView.textColor, NSUnderlineStyleAttributeName: 1]
        cell.textView.linkTextAttributes = attributes
        
        return cell
    }
    
    
    // View usernames above bubbles
    override func collectionView(collectionView: JSQMessagesCollectionView!, attributedTextForMessageBubbleTopLabelAtIndexPath indexPath: NSIndexPath!) -> NSAttributedString! {
        let message = messages[indexPath.item];
        
        // Sent by me, skip
        if message.senderId() == senderDisplayName {
            return nil;
        }
        
        // Same as previous sender, skip
        if indexPath.item > 0 {
            let previousMessage = messages[indexPath.item - 1];
            if previousMessage.senderId() == message.senderId() {
                return nil;
            }
        }
        
        return NSAttributedString(string:message.senderId())
    }
    
    override func collectionView(collectionView: JSQMessagesCollectionView!, layout collectionViewLayout: JSQMessagesCollectionViewFlowLayout!, heightForMessageBubbleTopLabelAtIndexPath indexPath: NSIndexPath!) -> CGFloat {
        let message = messages[indexPath.item]
        
        // Sent by me, skip
        if message.senderId() == senderDisplayName {
            return CGFloat(0.0);
        }
        
        // Same as previous sender, skip
        if indexPath.item > 0 {
            let previousMessage = messages[indexPath.item - 1];
            if previousMessage.senderId() == message.senderId() {
                return CGFloat(0.0);
            }
        }
        
        return kJSQMessagesCollectionViewCellLabelHeightDefault
    }
    
    // Received a new message, add it to our list
    private func receivedMessageCallback(msg: MatchMessage) {
        if converseWith.username != user {
            let message = Message(sender: msg.sender, text: msg.message)
            messages.append(message)
            collectionView.reloadData()
        }
    }
    
    // Load a list of messages from the server
    private func loadServerMessagesCallback(messageList: [MatchMessage]?) {
        if let tempMessages = messageList {
            for msg in tempMessages {
                let m = Message(sender: msg.sender, text: msg.message)
                messages.append(m)
            }
            
            collectionView.reloadData()
        }
    }
}