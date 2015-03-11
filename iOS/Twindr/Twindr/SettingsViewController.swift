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
    var genderBackend = ["M", "F", "X"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.genderPicker.dataSource = self
        self.genderPicker.delegate = self
        
        // Set the input for the fields
        self.datePickerView.datePickerMode = UIDatePickerMode.Date
        self.dobField.inputView = self.datePickerView
        self.genderField.inputView = self.genderPicker
        
        // Should grab info from server
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func saveButton(sender: UIButton) {
        // save the changes
    }
    
    @IBAction func editDobField(sender: UITextField) {
        self.datePickerView.addTarget(self, action: Selector("handleDatePicker:"), forControlEvents : UIControlEvents.ValueChanged)
    }
    
    func handleDatePicker(sender: UIDatePicker) {
        var dateFormatter = NSDateFormatter()
        dateFormatter.dateStyle = .ShortStyle
        dateFormatter.dateFormat = "yyyy/MM/dd"
        dobField.text = dateFormatter.stringFromDate(sender.date)
    }
    
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
