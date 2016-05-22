window.Speech = (function (undefined) {

    // https://github.com/aralejs/events ======================================

    var eventSplitter = /\s+/,
        Events = function () {}

    Events.prototype.on = function (events, callback, context) {
        var cache, event, list
        if (!callback) return this

        cache = this.__events || (this.__events = {})
        events = events.split(eventSplitter)

        while (event = events.shift()) {
            list = cache[event] || (cache[event] = [])
            list.push(callback, context)
        }

        return this
    }

    Events.prototype.off = function (events, callback, context) {
        var cache, event, list, i

        if (!(cache = this.__events)) return this
        if (!(events || callback || context)) {
            delete this.__events
            return this
        }

        events = events ? events.split(eventSplitter) : Object.keys(cache)

        while (event = events.shift()) {
            list = cache[event]
            if (!list) continue

            if (!(callback || context)) {
                delete cache[event]
                continue
            }

            for (i = list.length - 2; i >= 0; i -= 2) {
                if (!(callback && list[i] !== callback ||
                        context && list[i + 1] !== context)) {
                    list.splice(i, 2)
                }
            }
        }

        return this
    }

    Events.prototype.trigger = function(events) {
        var cache, event, all, list, i, len, rest = [], args
        if (!(cache = this.__events)) return this

        events = events.split(eventSplitter)

        for (i = 1, len = arguments.length; i < len; i++) {
            rest[i - 1] = arguments[i]
        }

        while (event = events.shift()) {
            if (all = cache.all) all = all.slice()
            if (list = cache[event]) list = list.slice()

            if (list) {
                for (i = 0, len = list.length; i < len; i += 2) {
                    list[i].apply(list[i + 1] || this, rest)
                }
            }

            if (all) {
                args = [event].concat(rest)
                for (i = 0, len = all.length; i < len; i += 2) {
                    all[i].apply(all[i + 1] || this, args)
                }
            }
        }

        return this
    }

    Events.mixTo = function(receiver) {
        receiver = receiver.prototype || receiver
        var proto = Events.prototype

        for (var p in proto) {
            if (proto.hasOwnProperty(p)) {
                receiver[p] = proto[p]
            }
        }
    }

    // Speech instance ========================================================

    function Speech (options) {

        // default options
        this.options = {
            debugging: false,
            continuous: false,
            interimResults: false,
            autoRestart: false
        }

        // merge user options
        if (Object.prototype.toString.call(options) === '[object Object]') {
            for (var op in options) {
                this.options[op] = options[op]
            }
        }

        this.active = false
        this.history = []
        this.lastIndex = -1
        this.lastResult = ''
        this.recognition = new webkitSpeechRecognition()

        var rec = this.recognition,
            self = this

        rec.continuous = self.options.continuous
        rec.interimResults = self.options.interimResults
        if (options.lang) rec.lang = options.lang

        rec.onstart = function () {
            self.active = true
            self.trigger('start')
        }

        rec.onresult = function (e) {
            if (!e.results || !e.results.length) return

            var updatedResult = e.results[e.resultIndex],
                transcript = updatedResult[0].transcript.replace(/^\s*/, '')

            // new sentence?
            if (e.resultIndex !== self.lastIndex) {
                self.lastIndex = e.resultIndex
                self.lastResult = ''
            }

            // avoid some redundancy
            if (transcript === self.lastResult && !updatedResult.isFinal) return
            if (transcript.length < self.lastResult.length) return

            self.lastResult = transcript

            if (updatedResult.isFinal) {
                // final sentence! we can do work!
                self.history.push(transcript)
                self.trigger('finalResult', transcript)
            } else {
                // interim, let's update stuff on screen
                self.trigger('interimResult', transcript)
            }
            
            if (self.options.debugging) {
                console.log(transcript + (updatedResult.isFinal ? ' (final)' : ''))
            }
        }

        rec.onerror = function (e) {
            self.trigger('error', e)
        }

        rec.onend = function () {
            self.active = false
            self.trigger('end')
            if (self.options.autoRestart) {
                self.start()
            }
        }

    }

    Speech.prototype.start = function () {
        this.recognition.start()
    }

    Speech.prototype.stop = function () {
        this.recognition.stop()
    }

    Events.mixTo(Speech)

    return Speech
})()