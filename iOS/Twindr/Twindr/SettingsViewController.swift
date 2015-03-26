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
    @IBOutlet weak var interestsField: UITextField!
    
    var datePickerView: UIDatePicker = UIDatePicker()
    var genderPicker: UIPickerView = UIPickerView()
    var genders = ["Male", "Female", "Prefer not to say..."]
    var genderBackend = ["Male":"M", "Female":"F", "Prefer not to say":"X"]
    
    var isRegistration = false
    
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
        self.datePickerView.datePickerMode = UIDatePickerMode.Date
        self.dobField.inputView = self.datePickerView
        self.dobField.inputAccessoryView = toolbar
        
        // Set the inputs for the genderPicker
        self.genderField.inputView = self.genderPicker
        self.genderField.inputAccessoryView = toolbar
        
        // Should grab info from server and set values
        if(!isRegistration) {
            var profileInfo: UpdateRegistration! = getProfileInformation(token: "cbb5e4f8-66ca-49a2-8cdc-89789d5e66f1")
            interests = profileInfo.interests
        }
        
        // Retrieve previously entered info
        if(gender != nil){
            genderField.text = gender
        }
        if(dob != nil){
            dobField.text = dob
        }
        if(interests != nil){
            interestsField.text = interests
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func saveButton(sender: UIButton) {
        // save the changes
        if(genderField.text != "" && dobField.text != "") {
            gender = genderField.text
            dob = dobField.text
            interests = interestsField.text
            
            // Send to server
            if(isRegistration) {
                sendBusinessObject(Registration(sex: genderBackend[gender]!, dateOfBirth: dob, interests: interests), "/m/registerUser")(token: "cbb5e4f8-66ca-49a2-8cdc-89789d5e66f1")
            } else {
                sendBusinessObject(UpdateRegistration(interests: interests), "/m/registerUser")(token: "cbb5e4f8-66ca-49a2-8cdc-89789d5e66f1")
            }
        }
        
        // If registration, send to home page
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
