var Util = (function() {
	var uid = ['0', '0', '0'];
	return {
		/* return the guid which is unique to the page and it will never overflow as it is a string*/
		nextUid : function() {
			var index = uid.length;
			var digit;

			while (index) {
				index--;
				digit = uid[index].charCodeAt(0);
				if (digit == 57 /* '9' */) {
					uid[index] = 'A';
					return uid.join('');
				}
				if (digit == 90 /* 'Z' */) {
					uid[index] = '0';
				} else {
					uid[index] = String.fromCharCode(digit + 1);
					return uid.join('');
				}
			}
			uid.unshift('0');
			return uid.join('');
		},
		/* return the array with variable name */
		annotation: function(fn) {
			var FN_ARG = /^\s*(_?)(\S+?)\1\s*$/,
				STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg,
				$inject,
				fnText,
				argDecl;
			
			if (typeof fn == 'function') {    
				$inject = [];
				if (fn.length) {
					fnText = fn.toString().replace(STRIP_COMMENTS, '');
					argDecl = fnText.match(FN_ARGS);
					argDecl[1].split(FN_ARG_SPLIT).forEach(function(arg){
						arg.replace(FN_ARG, function(all, underscore, name){
							$inject.push(name);
						});
					});
				}
			}
			return $inject;
		},
		
		namespace : function(){
            var len1 = arguments.length,
                i = 0,
                len2,
                j,
                main,
                ns,
                sub,
                current;

            for(; i < len1; ++i) {
                main = arguments[i];
                ns = arguments[i].split('.');
                current = window[ns[0]];
                if (current === undefined) {
                    current = window[ns[0]] = {};
                }
                sub = ns.slice(1);
                len2 = sub.length;
                for(j = 0; j < len2; ++j) {
                    current = current[sub[j]] = current[sub[j]] || {};
                }
            }
            return current;
        }
	};
})();