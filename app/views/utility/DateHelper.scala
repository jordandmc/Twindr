package views.utility

import play.twirl.api.Html

/**
 * Helper utilities for dates
 */
object DateHelper {

  /**
   * Date picker script
   * @return Required HTML for including jQuery's date picker
   */
  def includeJQueryDateTime = {
    Html("""
      <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
          <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
            <script type="text/javascript">
              $(function() {
                $("#datepicker").datepicker({
                  dateFormat: "yy-mm-dd",
                  changeMonth: true,
                  changeYear: true,
                  yearRange: "-120:+0",
                  maxDate: "-8y"
                });
              });
            </script>
         """);
  }

}
