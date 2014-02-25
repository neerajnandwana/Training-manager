'use strict';

app.directives.directive('confirmationNeeded', function () {
	return {
		priority: 1,
	    terminal: true,
		link: function (scope, element, attr) {
			var msg = attr.confirmationNeeded || "Are you sure?",
				clickAction = attr.ngClick,
				template = ['<div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">',
							  '<div class="modal-dialog">',
							    '<div class="modal-content">',
							      '<div class="modal-header">',
							        '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>',
							        '<h4 class="modal-title">Confirmation</h4>',
							      '</div>',
							      '<div class="modal-body">', msg, '</div>',
							      '<div class="modal-footer">',
							        '<button type="button" class="btn btn-primary">Ok</button>',
							        '<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>',
							      '</div>',
							    '</div>',
							  '</div>',
							'</div>'].join(''),
				$template = $(template);		
			
			$template.find('button:not([data-dismiss])').bind('click', function(){
				$template.modal('hide');
				scope.$eval(clickAction);
			});	
			
			element.bind('click',function (e) {
				$template.modal('show');
				e.stopPropagation();
				e.preventDefault();				
			});
		}
	};
});