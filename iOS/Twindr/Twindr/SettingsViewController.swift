//
//  SettingsViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-02-12.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit

class SettingsViewController: ViewController, UIPickerViewDelegate, UIPickerViewDataSource {
    
    @IBOutlet weak var genderField: UITextField!
    @IBOutlet weak var dobField: UITextField!
    @IBOutlet weak var interestsField: UITextView!
    @IBOutlet weak var genderLabel: UILabel!
    @IBOutlet weak var dobLabel: UILabel!
    
    
    var datePickerView: UIDatePicker = UIDatePicker()
    var genderPicker: UIPickerView = UIPickerView()
    var genders = ["Male", "Female", "Prefer not to say"]
    var genderBackend = ["Male":"M", "Female":"F", "Prefer not to say":"X"]
    
    var isRegistration = false
    var registrationAlert: UIAlertView = UIAlertView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.genderPicker.dataSource = self
        self.genderPicker.delegate = self
        
        // Create toolbar
        var toolbar = UIToolbar()
        toolbar.barStyle = UIBarStyle.Default
        toolbar.translucent = true
        toolbar.sizeToFit()
        
        // Create items to put onto the toolbar
        var spaceButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.FlexibleSpace, target: nil, action: nil)
        var doneButton = UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.Bordered, target: self, action: "donePressed")
        
        // Add items to the toolbar
        toolbar.setItems([spaceButton, doneButton], animated: false)
        
        // Set the inputs for the datePicker
        datePickerConfig()
        
        // Set the inputs for the dob and gender fields
        self.dobField.inputView = self.datePickerView
        self.dobField.inputAccessoryView = toolbar
        self.genderField.inputView = self.genderPicker
        self.genderField.inputAccessoryView = toolbar
        
        // Make the text View look like a textfield by changing corners/colours
        self.interestsField.layer.borderColor = UIColor(red: 0.9, green: 0.9, blue: 0.9, alpha: 1.0).CGColor
        self.interestsField.layer.borderWidth = 1.0;
        self.interestsField.layer.cornerRadius = 5.0;
        
        // Should grab info from server and set values
        if let tkn = xAuthToken {
            if !isRegistration {
                Curried().getProfileInformation(token: tkn, callback: populateInterestsCallback)
            }
        }
        
        // Retrieve previously entered info
        if(gender != nil) {
            genderField.text = gender
        }
        if(dob != nil) {
            dobField.text = dob
        }
        if(interests != nil) {
            interestsField.text = interests
        }
        
        // Handle registration errors
        registrationAlert.delegate = self
        registrationAlert.title = "Empty Text Field!"
        registrationAlert.message = "The Gender and Date of Birth fields cannot be empty!"
        registrationAlert.cancelButtonIndex = registrationAlert.addButtonWithTitle("ok")

        
        // If not registration, don't allow editting of certain fields
        if(!isRegistration) {
            genderLabel.hidden = true
            genderField.hidden = true
            dobLabel.hidden = true
            dobField.hidden = true
        }
    }
    
    func populateInterestsCallback(res: UpdateRegistration?) {
        if let info = res {
            interestsField.text = info.interests
        }
    }
    
    func datePickerConfig() {
        //Set Maximum Date
        self.datePickerView.datePickerMode = UIDatePickerMode.Date
        let dateComponents = NSDateComponents()
        dateComponents.year = -8
        let eightYearsAgo = NSCalendar.currentCalendar().dateByAddingComponents(dateComponents, toDate: NSDate(), options: nil)
        self.datePickerView.maximumDate = eightYearsAgo
        
        //Set Minimum Date
        dateComponents.year = -120
        let oneTwentyYearsAgo = NSCalendar.currentCalendar().dateByAddingComponents(dateComponents, toDate: NSDate(), options: nil)
        self.datePickerView.minimumDate = oneTwentyYearsAgo
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func saveButton(sender: UIButton) {
        // save the changes
            interests = interestsField.text
            
        // Send to server
        if let tkn = xAuthToken {
            if(isRegistration && genderField.text != "" && dobField.text != "") {
                gender = genderField.text
                dob = dobField.text
                Curried().register(obj: Registration(sex: genderBackend[gender]!, dateOfBirth: dob, interests: interests), token: tkn)
            } else if(!isRegistration) {
                Curried().updateRegistration(obj: UpdateRegistration(interests: interests), token: tkn)
            } else {
                registrationAlert.show()
                return
            }
            
            let navigationController = self.storyboard?.instantiateViewControllerWithIdentifier("StartNav") as UINavigationController
            self.presentViewController(navigationController, animated: true, completion: nil)
        }
        
    }
    
    // Removes the pickers from view if they're active
    func donePressed() {
        if(genderField.isFirstResponder()) {
            if(genderField.text == "") {
                genderField.text = genders[0]
            }
            genderField.resignFirstResponder()
            
        } else if(dobField.isFirstResponder()) {
            if(dobField.text == "") {
                handleDatePicker(self.datePickerView)
            }
            dobField.resignFirstResponder()
        }
    }
    
    // DatePicker functions
    @IBAction func editDobField(sender: UITextField) {
        self.datePickerView.addTarget(self, action: Selector("handleDatePicker:"), forControlEvents : UIControlEvents.ValueChanged)
    }
    
    func handleDatePicker(sender: UIDatePicker) {
        var dateFormatter = NSDateFormatter()
        dateFormatter.dateStyle = .ShortStyle
        dateFormatter.dateFormat = "yyyy/MM/dd"
        dobField.text = dateFormatter.stringFromDate(sender.date)
    }

    /*
     * PickerView functions
     */
    // UIPickerViewDataSource
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        return 1;
    }
    
    // UIPickerViewDataSource
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return self.genders.count;
    }
    
    // UIPickerViewDelegate
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String {
        return self.genders[row]
    }

    // UIPickerViewDelegate
    func pickerView(pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int)
    {
        genderField.text = self.genders[row]
    }
}
